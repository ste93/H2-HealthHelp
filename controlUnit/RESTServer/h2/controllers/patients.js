var request = require('request');

////////////// Patient List Page /////////////////////////////////////
var renderPatientHomepage = function(req, res, responseBody){

  var patientList = JSON.parse(responseBody);
  res.render('patients', {
    title: 'H2 Patient Manager',
    patientList: patientList
  });
};

/* GET sensors page */
module.exports.patientsHome = function(req, res){
  request.get('http://localhost:3000/api/patients', function (error, response, body) {
    console.log('error:', error); // Print the error if one occurred
    console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
    if (body)
      renderPatientHomepage(req,res, body);
  });
};
//////////////////////////////////////////////////////////////////////

////////////// Add Patient Page /////////////////////////////////////
module.exports.addNew = function(req,res) {
  var postData = {
    id : req.params.patientID,
    name : req.body.name,
  }

  request({
    url: 'http://localhost:3000/api/patients/',
    method: 'POST',
    json: postData
    }, function(error, response, body){
      if (response.statusCode === 201) {
        res.redirect('/patients/' +  req.params.sensorID);
      }
  });
}
//////////////////////////////////////////////////////////////////////

/////////////////////////////// Detail Page //////////////////////////
  var renderPatientDetailPage = function(req, res, responseBody){
    var body = JSON.parse(responseBody);
    res.render('patients-details', body[0] );
  }

  /* GET detail page */
  module.exports.details = function(req, res){
    request.get('http://localhost:3000/api/patients/' + req.params.patientID, function (error, response, body) {
      console.log('error:', error); // Print the error if one occurred
      console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received

      renderPatientDetailPage(req,res, body);
    });
  };
//////////////////////////////////////////////////////////////////////

/////////////////////////////// Delete OBJ //////////////////////////
module.exports.delete = function(req,res) {
  request.delete('http://localhost:3000/api/patients/' + req.params.sensorID, function (error, response, body) {
    console.log('error:', error); // Print the error if one occurred
    console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
    res.redirect('/sensors/');
  });
}

//////////////////////////////////////////////////////////////////////
