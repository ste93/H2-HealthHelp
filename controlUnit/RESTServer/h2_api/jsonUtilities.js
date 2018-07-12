/**
* Some generic functions for Json handling
*/

/**
* Send a Response to an HTTP request using Json as format.
*  @param res the response object related to the specific request.
*  @param status the standard Response Code to send back.
*  @param content {Json / String} the content of the response. It's parsed in Json format.
*/
module.exports.sendJsonResponse = function(res, status, content) {
  res.status(status);
  res.json(content);
};
