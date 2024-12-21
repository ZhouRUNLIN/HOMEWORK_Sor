const { Client } = require('pg')

const client = new Client({
  user: process.env.PGUSER,
  host: process.env.PGHOST,
  database: process.env.PGDATABASE,
  port: process.env.PGPORT,
})

const connect = async () =>
  client.connect().catch(err => {
    console.error(
      `Postgres: Connection refused to ${err.address + ':' + err.port}`
    )
  })

const end = async () => {
  await client.end()
}

const query = (...args) => client.qery(args)

module.exports = { client, connect, end, query }
