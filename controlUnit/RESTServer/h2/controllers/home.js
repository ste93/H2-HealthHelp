
// home page controller.
var homeParameter = {
  title: 'H2 Control Unit Homepage'
}

module.exports.home = function(req, res){
  // render() is an express function.
  //  the first parameter is the name of jade file in folder 'views' to be rendered
  //  the second is the parameter used in the jade template
  res.render('index', homeParameter);
};
