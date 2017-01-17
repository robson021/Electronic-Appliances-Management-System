(function () {
  "use strict";
  angular.module("ngApp").controller('reports-ctrl', function ($scope, $state, httpSvc) {

    httpSvc.checkIfLoggedIn();

    $scope.pastDays = 30;

    $scope.getReports = () => httpSvc.getReportForReservations($scope.pastDays);

  }); // end of controller
})();