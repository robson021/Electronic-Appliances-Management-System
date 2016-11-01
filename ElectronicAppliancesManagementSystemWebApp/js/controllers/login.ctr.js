(function () {
    "use strict";
    angular.module("ngApp").controller('login-register-ctrl', function ($scope, httpSvc) {


        $scope.user = {
            email: '',
            name: '',
            surname: '',
            password: ''
        };

        $scope.login = function () {
            httpSvc.loginUser($scope.user);
            httpSvc.getAllBuildings();
        }

    }); // end of controller
})();