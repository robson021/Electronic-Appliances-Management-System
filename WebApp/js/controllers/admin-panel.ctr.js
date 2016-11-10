(function () {
    "use strict";
    angular.module("ngApp").controller('admin-panel-ctrl', function ($scope, $rootScope, $state, httpSvc) {

        httpSvc.checkIfLoggedAsAdmin();

        const possibleViews = ['activatedAcc', 'deactivatedAcc'];

        $scope.currentView = null;


        $scope.loadActivatedUsers = function () {
            httpSvc.getAllActivatedAccounts();
            $scope.currentView = possibleViews[0];
        };

        $scope.loadInactivatedUsers = function () {
            httpSvc.getAllInactiveAccounts();
            $scope.currentView = possibleViews[1];
        };


    }); // end of controller
})();