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

    $scope.makeReservation = function () {
      /*if (!$scope.validateDate()) {
       toastr.error("Invalid time.");
       return;
       }*/
      let date = $scope.timeOfReservation;
      date.setHours($scope.hour);
      date.setMinutes($scope.minutes);
      let time = {
        "from": date.getTime(),
        "minutes": $scope.forHowLong
      };

      httpSvc.newReservation(time, $scope.appliance.id);
    };

    $scope.validateDate = function () {
      return !($scope.hour > 23 || $scope.hour < 0 || $scope.minutes < 0 || minutes > 59);
    };

  }); // end of controller
})();