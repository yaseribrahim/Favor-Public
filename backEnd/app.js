const express = require('express')
const mongoose = require('mongoose')
const database = require('./database')
const http = require('http')
const config = require('./config')
const Routes = require('./routes')
const Events = require('./events')


//set up express app
const app = express()

//load middleware
app.use(express.json())

// load endpoint routes
app.use('/', Routes)

// connect to Database
const connection = database()

// create server instance
const server = http.Server(app)

// create socket event handlers
const sockEvents = Events(server)

// listen
server.listen(config.LISTEN_PORT, () => {
  console.log(`Express app listening on *:${config.LISTEN_PORT}`)
})
