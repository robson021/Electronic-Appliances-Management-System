(function () {
    "use strict";
    angular.module("ngApp").controller('guest-panel-ctrl', function ($scope, httpSvc) {

        $scope.token = '';

        $scope.submitToken = function () {
            if ($scope.token.length < 10) {
                return;
            }
            httpSvc.submitTokenAsGuest($scope.token);
        };


    }); // end of controller
})();