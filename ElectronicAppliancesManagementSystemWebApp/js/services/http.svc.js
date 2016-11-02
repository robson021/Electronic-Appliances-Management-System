app.service('httpSvc', function ($rootScope, $http, $state) {

    const url = $rootScope.serverAddress;

    this.loginUser = function (user) {
        let uri = '/login/' + user.email + '/' + user.password + '/';
        $http.post(url + uri, null)
            .success(function (response) {
                console.info(response);
                if (response === 'OK') {
                    $state.go('default');
                    $rootScope.loggedIn = true;
                    toastr.success('You have been logged in.');
                    //self.clearData(user);
                } else {
                    toastr.error('Error. Try again.')
                }

            });
    };

    this.getAllBuildings = function () {
        let uri = '/user-service/get-all-buildings/';
        $http.get(url + uri, null)
            .success(function (response) {
                console.info(response);
            });
    };

    /*clearObject = function(obj) {
     for (let member in obj) {
     delete obj[member];
     }
     }*/
});