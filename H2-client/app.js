//import { error } from 'util';

var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var session = require('client-sessions');

var indexRouter = require('./routes/index');

var app = express();

app.use(session({
  secret: '0GBlJZ9EKBt2Zbi2flRPvztczCewBxXK' 
}));

app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname + '/public')));
//app.use("/public", express.static(path.join(__dirname, 'public')));
console.log("dirname" + path.join(__dirname ,'/public'))
app.use('/', indexRouter);
app.use('/bootstrap', express.static(__dirname + '/node_modules/bootstrap/dist/'));
app.use('/jquery', express.static(__dirname + '/node_modules/jquery/dist/'));
app.use('/socket.io', express.static(__dirname + '/node_modules/socket.io-client/dist/'));

// catch 404 and forward to error handler

app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  console.log("SONO IN APP req: " + req);
  console.log("SONO IN APP res: " + err.stack);
  console.log("SONO IN APP next: " + next);
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
