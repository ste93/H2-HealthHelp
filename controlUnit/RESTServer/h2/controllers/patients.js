/**
* Controller methods to handle the patients-related pages.
*/

var request = require('request');

////////////// Patient List Page ///////////////////////////////////////////////
/**
* Express function to render patients management homepage.
* @param req Is the HTTP Request. Not used by this function.
* @param res Is the HTTP Response, returned to the requesting host with the result.
* @param patientList is a JSON list with the data of all the patient registered to the current control unit.
* Accepted list must have format like : [{"id" : String, "name": String}, ...]
*/
var renderPatientHomepage = function(req, res, patientList){
  res.render('patients', {
    title: 'H2 Patient Manager',
    patientList: patientList
  });
};

/**
* GET main patients management page
* @param req Is the HTTP Request. Not used by this function.
* @param res Is the HTTP Response, returned to the requesting host with the rendered page.
*/
module.exports.patientsHome = function(req, res){
  // request to the REST Service the list of the currently registered patient.
  request.get('http://localhost:3000/api/patients', function (error, response, body) {
    if(error)
      console.log('error:', error);
    console.log('statusCode:', response && response.statusCode);
    if (body){
      var patientList = JSON.parse(body);
      renderPatientHomepage(req,res, patientList);
   }
   //TODO: GESTIONE DEGLI ERRORI -> RENDER PAGINA ERRORE
  });
};
////////////////////////////////////////////////////////////////////////////////

/////////// Detail Page ////////////////////////////////////////////////////////
/**
* Express function to render details page for a patient.
* @param req Is the HTTP Request. Not used by this function.
* @param res Is the HTTP Response, returned to the requesting host with the the rendered page.
* @param patientDetails is a JSON Object with the details of the specified patient.
* Accepted object must have format like : {"id" : String, "name": String}
*/
  var renderPatientDetailPage = function(req, res, patientDetails){
    res.render('patients-details', patientDetails );
  }

  /**
  * GET page with patient's details for a specific patient.
  * @param req Is the HTTP Request. Contains patientID of the patient whose details is searched.
  * @param res Is the HTTP Response, returned to the requesting host with the rendered page.
  */
  module.exports.details = function(req, res){
    // Query REST service for a specific patient's details.
    request.get('http://localhost:3000/api/patients/' + req.params.patientID, function (error, response, responseBody) {
      if(error)
        console.log('error:', error);
      console.log('statusCode:', response && response.statusCode);
      console.log('body:', responseBody );
      if (responseBody){
        //console.log('resp body:', responseBody );
        var patientDetails = JSON.parse(responseBody);
        //console.log('parsed body:', body );
        renderPatientDetailPage(req,res, patientDetails);
      }
      //TODO: GESTIONE DEGLI ERRORI -> RENDER PAGINA ERRORE
    });
  };
//////////////////////////////////////////////////////////////////////

////////////// Add Patient /////////////////////////////////////////////////////
/**
* Express function to render patient addition form.
* @param req Is the HTTP Request. Not used by this function.
* @param res Is the HTTP Response, returned to the requesting host with the rendered page.
*/
var renderPatientForm = function( req, res) {
  res.render('patients-new',  {
    title: 'H2 new patient page'
  });
}

/**
* GET form for new patient registration.
*/
module.exports.newPatientForm = function (req,res){
  renderPatientForm(req,res);
}

/**
* POST request for new patient registration. Invocked by default when the patient's form is submitted.
* @param req Is the HTTP POST Request. Contain the data insered by user in the form.
* @param res Is the HTTP Response, returned to the requesting host with the rendered page.
*/
module.exports.addNew = function(req,res) {
  // retrieve data from the submitted form.
  var postData = {
    id : req.body.id,
    name : req.body.name,
  }

  // Send a POST request the the REST service in order to add the new registered patient.
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

////////////////////////////////////////////////////////////////////////////////

/////////////////////////////// Delete patient /////////////////////////////////
/**
* DELETE request for a specific patient.
* @param req Is the HTTP Request. Used to retrieve the patientID of the patient to be deleted.
* @param res Is the HTTP Response, returned to the requesting host with the rendered page.
*/
module.exports.delete = function(req,res) {
  // DELETE request to the REST service.
  request.delete('http://localhost:3000/api/patients/' + req.params.sensorID, function (error, response, body) {
    if(error)
      console.log('error:', error);
    console.log('statusCode:', response && response.statusCode);
    // return to the main patients page after deletion.
    res.redirect('/patients/');
  });
}

////////////////////////////////////////////////////////////////////////////////
