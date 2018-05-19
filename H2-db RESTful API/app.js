// risorse esterne da includere 
// più importanti sono modulo express e due file locali che contengono le rotte
var createError = require('http-errors');
var express = require('express');
//var session = require('express-session');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

var indexRouter = require('./routes/index');
var applicationRouter = require('./routes/H2routing');

var app = express();

// dico all'app dove sono le view
app.set('views', path.join(__dirname, 'views'));
// e in che formato sono
app.set('view engine', 'jade');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
// dico all'app di servire tutto il contenuto della cartella 'public' come statico
app.use(express.static(path.join(__dirname, 'public')));
//app.use(session({secret: 'login'}));

// definisco i namespace base per due tipologie di rotte: routes e users sono 'router' di rotte
app.use('/', indexRouter);
app.use('/database/application', applicationRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'production' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
