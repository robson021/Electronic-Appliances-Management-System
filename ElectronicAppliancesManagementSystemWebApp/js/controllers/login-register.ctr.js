(function () {
    "use strict";
    angular.module("ngApp").controller('login-register-ctrl', function ($scope, $rootScope, $state, httpSvc) {

        $scope.user = {
            email: '',
            name: '',
            surname: '',
            password: '',
            repassword: ''
        };

        $scope.showRegisterView = false;

        $scope.login = function () {
            httpSvc.loginUser($scope.user);
        };

        $scope.registerView = function () {
            $scope.showRegisterView = true;
        };

        $scope.backToLogin = function () {
            $scope.showRegisterView = false;
            if ($rootScope.loggedIn) {
                $state.go('default');
            }
        };

        $scope.register = function () {
            httpSvc.registerNewUser($scope.user);
        };

    }); // end of controller
})();