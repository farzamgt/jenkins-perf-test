const fs = require('fs')
const puppeteer = require('puppeteer')

const billingData = JSON.parse(fs.readFileSync('./billing-data.json', 'utf8'))

async function captureReport() {
  const lighthouse = await import('lighthouse')
  const { startFlow, desktopConfig } = lighthouse

  const browser = await puppeteer.launch({
    headless: true,
    args: ['--no-sandbox', '--disable-gpu']
  })

  const page = await browser.newPage()
  await page.setViewport({ width: 1366, height: 768 })
  await page.setDefaultTimeout(15000)

  const baseUrl = 'http://wp:80'

  const flow = await startFlow(page, {
    name: 'order-flow-5-iterations',
    config: desktopConfig,
    flags: { screenEmulation: { disabled: true } },
    configContext: {
      settingsOverrides: {
        onlyCategories: ['performance', 'accessibility', 'best-practices', 'seo']
      }
    }
  })

  for (let i = 1; i <= 5; i++) {
    console.log(`===== ITERATION ${i} =====`)

    await flow.navigate(baseUrl, { stepName: `home ${i}` })

    const tablesUrl = await page.evaluate(() =>
      document.querySelector('li.page-item-13 a')?.href
    )
    await flow.navigate(tablesUrl, { stepName: `tables ${i}` })

    const productUrl = await page.evaluate(() =>
      document.querySelector('a[href*="/products/"]')?.href
    )
    await flow.navigate(productUrl, { stepName: `product ${i}` })

    await flow.startTimespan({ stepName: `add to cart ${i}` })
    await page.click('button.button.green-box.ic-design')
    await new Promise(r => setTimeout(r, 1500))
    await flow.endTimespan()

    const cartUrl = await page.evaluate(() =>
      document.querySelector('li.page-item-31 a')?.href
    )
    await flow.navigate(cartUrl, { stepName: `cart ${i}` })

    const checkoutUrl = await page.evaluate(() =>
      document.querySelector('input.to_cart_submit.button.green-box.ic-design')?.form?.action
    )
    await flow.navigate(checkoutUrl, { stepName: `checkout ${i}` })

    await flow.startTimespan({ stepName: `fill form ${i}` })
    await page.type('input[name="cart_name"]', billingData.name)
    await page.type('input[name="cart_address"]', billingData.address)
    await page.type('input[name="cart_postal"]', billingData.postal)
    await page.type('input[name="cart_city"]', billingData.city)
    await page.select('select[name="cart_country"]', billingData.country)
    await page.type('input[name="cart_phone"]', billingData.phone)
    await page.type('input[name="cart_email"]', billingData.email)
    await flow.endTimespan()

    await page.click('input[name="cart_submit"]')

    await page.waitForFunction(() =>
      window.location.href.includes('thank-you'),
      { timeout: 15000 }
    )

    await flow.navigate(page.url(), { stepName: `submit order ${i}` })
  }

  // REPORTS

  const htmlReport = await flow.generateReport({ format: 'html' })
  fs.writeFileSync('order-flow-5-iterations.html', htmlReport)

  const flowResult = await flow.createFlowResult()

  fs.writeFileSync(
    'order-flow-5-iterations.json',
    JSON.stringify(flowResult, null, 2)
  )

  // -------- AGGREGATION BY PAGE --------

  const grouped = {}

  for (const step of flowResult.steps) {
    if (!step.lhr) continue

    const baseName = step.name.replace(/\s\d+$/, '')

    if (!grouped[baseName]) grouped[baseName] = []

    grouped[baseName].push({
      performance: step.lhr.categories.performance.score,
      lcp: step.lhr.audits['largest-contentful-paint']?.numericValue,
      fcp: step.lhr.audits['first-contentful-paint']?.numericValue,
      tbt: step.lhr.audits['total-blocking-time']?.numericValue,
      cls: step.lhr.audits['cumulative-layout-shift']?.numericValue
    })
  }

  function aggregate(arr, field) {
    const values = arr
      .map(x => x[field])
      .filter(v => typeof v === 'number')

    if (!values.length) return null

    return {
      avg: values.reduce((a, b) => a + b, 0) / values.length,
      min: Math.min(...values),
      max: Math.max(...values)
    }
  }

  const summaryByPage = {}

  for (const pageName in grouped) {
    summaryByPage[pageName] = {
      performance: aggregate(grouped[pageName], 'performance'),
      lcp: aggregate(grouped[pageName], 'lcp'),
      fcp: aggregate(grouped[pageName], 'fcp'),
      tbt: aggregate(grouped[pageName], 'tbt'),
      cls: aggregate(grouped[pageName], 'cls')
    }
  }

  fs.writeFileSync(
    'order-flow-aggregated-by-page.json',
    JSON.stringify(summaryByPage, null, 2)
  )

  console.log('Reports generated:')
  console.log('- order-flow-5-iterations.html')
  console.log('- order-flow-5-iterations.json')
  console.log('- order-flow-aggregated-by-page.json')

  await browser.close()
}

captureReport()