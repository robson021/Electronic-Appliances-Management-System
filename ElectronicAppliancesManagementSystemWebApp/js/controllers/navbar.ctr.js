(function () {
    "use strict";
    angular.module("ngApp").controller('navbar-ctrl', function ($scope, httpSvc) {

        $scope.logout = function () {
            httpSvc.logoutUser();
        };

    }); // end of controller
})();