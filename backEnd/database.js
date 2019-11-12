let mongoose = require('mongoose')
var config = require('./config');


function startConnection() {
  mongoose.connect(`${config.MONGO_URI}`, { useNewUrlParser: true })
  .then(function () {
    console.log('Database connection successful')
  })
  .catch(function (err) {
    console.error('Database connection error')
  })
  return mongoose
}


module.exports = startConnection
