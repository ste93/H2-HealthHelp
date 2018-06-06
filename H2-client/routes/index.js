var express = require('express');
var router = express.Router();

var prova = require('../web_server/prova');
router.get('/', prova.connectToTopic);


module.exports = router;
