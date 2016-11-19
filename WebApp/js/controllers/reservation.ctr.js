(function () {
    "use strict";
    angular.module("ngApp").controller('reservation-ctrl', function ($scope, $rootScope, $state, httpSvc) {

        httpSvc.checkIfLoggedIn();

        $scope.timeOfReservation = new Date();
        $scope.hour = $scope.timeOfReservation.getHours();
        $scope.minutes = $scope.timeOfReservation.getMinutes();
        $scope.forHowLong = 60;

        $scope.appliance = $rootScope.selectedAppliance;
        $scope.allReservations = $scope.appliance.reservations;

    }); // end of controller
})();