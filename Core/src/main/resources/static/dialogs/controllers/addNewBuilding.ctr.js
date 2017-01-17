(function () {
  "use strict";
  angular.module("ngApp").controller('add-building-ctrl', function ($scope, ngDialog, httpSvc) {

    httpSvc.checkIfLoggedIn();

    $scope.inputField = '';

    $scope.addBuilding = function () {
      httpSvc.addNewBuilding($scope.inputField);
      ngDialog.closeAll(null);
    };

  }); // end of controller
})();