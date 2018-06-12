/**
* Controller methods to handle the home page.
*/

var homeParameter = {
  title: 'H2 Control Unit Homepage'
}

/**
* Function that return the home page's HTML render to the requester.
* @param req Is the HTTP Request. Not used by this function.
* @param res Is the HTTP Response, returned to the requesting host with the result.
*/
module.exports.home = function(req, res){
  // render() is an express function.
  //  the first parameter is the name of jade file in folder 'views' to be rendered
  //  the second is the parameters list used in the jade template
  res.render('index', homeParameter);
};
