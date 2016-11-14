(function () {
    "use strict";
    angular.module("ngApp").controller('reservation-ctrl', function ($scope, $rootScope, $state, httpSvc) {

        httpSvc.checkIfLoggedIn();

        $scope.appliance = $rootScope.selectedAppliance;
        $scope.allReservations = $scope.appliance.reservations;

    }); // end of controller
})();