(function () {
    "use strict";
    angular.module("ngApp").controller('login-ctrl', function ($scope, $http) {


        $scope.user = {
            email: '',
            password: ''
        };
        var user = $scope.user;
        $scope.loginUser = function () {
            console.info(user);
            var url = 'http://localhost:8080/login/' + user.email + "/" + user.password + '/';
            $http.post(url, null)
                .success(function (response) {
                    console.info(response);
                });
        };

        $scope.getAllBuildings = function () {
            var url = 'http://localhost:8080/user-service/get-all-buildings/';
            $http.get(url, null)
                .success(function (response) {
                    console.info(response)
                });
        }

    }); // end of controller
})();