app.service('httpSvc', function ($rootScope, $http, $state) {

    var url = $rootScope.serverAddress;

    this.loginUser = function (user) {
        let uri = '/login/' + user.email + '/' + user.password + '/';
        $http.post(url + uri, null)
            .success(function (response) {
                console.info(response);
                $state.go('default');
            });
    };

    this.getAllBuildings = function () {
        let uri = '/user-service/get-all-buildings/';
        $http.get(url + uri, null)
            .success(function (response) {
                console.info(response);
            });
    }
});