(function () {
    "use strict";
    angular.module("ngApp").controller('user-panel-ctrl', function ($scope, $rootScope, $state, httpSvc) {

        httpSvc.checkIfLoggedIn();

        $scope.allBuildings = null;

        $scope.getBuildings = function () {
            httpSvc.getAllBuildings($scope.allBuildings);
        };

    }); // end of controller
})();