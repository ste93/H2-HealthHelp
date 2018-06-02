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

////////////// Add Patient ///////////////////////////////////////////

var renderPatientForm = function( req, res) {
  res.render('patients-new',  {
    title: 'H2 new patient page'
  });
}

module.exports.newPatientForm = function (req,res){
  renderPatientForm(req,res);
}

module.exports.addNew = function(req,res) {
  var postData = {
    id : req.body.id,
    name : req.body.name,
  }

  request({
    url: 'http://localhost:3000/api/patients/',
    method: 'POST',
    json: postData
    }, function(error, response, body){
      if (response.statusCode === 201) {
        res.redirect('/patients/' +  req.body.id);
      }
  });
}
//////////////////////////////////////////////////////////////////////

/////////////////////////////// Detail Page //////////////////////////
  var renderPatientDetailPage = function(req, res, responseBody){
    console.log('resp body:', responseBody );
    var body = JSON.parse(responseBody);
    console.log('parsed body:', body );
    res.render('patients-details', body );
  }

  /* GET detail page */
  module.exports.details = function(req, res){
    request.get('http://localhost:3000/api/patients/' + req.params.patientID, function (error, response, body) {
      console.log('error:', error); // Print the error if one occurred
      console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
      console.log('body:', body );
      renderPatientDetailPage(req,res, body);
    });
  };
//////////////////////////////////////////////////////////////////////

/////////////////////////////// Delete OBJ //////////////////////////
module.exports.delete = function(req,res) {
  request.delete('http://localhost:3000/api/patients/' + req.params.sensorID, function (error, response, body) {
    console.log('error:', error); // Print the error if one occurred
    console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
    res.redirect('/patients/');
  });
}

//////////////////////////////////////////////////////////////////////
