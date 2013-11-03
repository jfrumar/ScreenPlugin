var screenControl = {
    keepOn: function(bool, successCallback, errorCallback) {
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'Screen', // mapped to our native Java class called "Screen"
            'keepOn', // with this action name
            [bool]
        ); 
    }
}
module.exports = screenControl;