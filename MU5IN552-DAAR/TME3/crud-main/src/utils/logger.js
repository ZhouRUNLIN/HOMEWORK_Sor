const regex = new RegExp(/at \w*/g)
const getFun = str => str.match(regex)[1]

const logger = func => (...val) => {
  const now = new Date(Date.now()).toISOString()
  const errorFunction = getFun(new Error().stack)
  func(`[${now}](${errorFunction}): `, ...val)
}

const log = logger(console.log)
const error = logger(console.error)
const info = logger(console.info)
const warn = logger(console.warn)

module.exports = {
  log,
  error,
  info,
  warn,
}
