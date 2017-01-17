(function () {
  "use strict";
  angular.module("ngApp").controller('add-room-ctrl', function ($scope, $rootScope, ngDialog, httpSvc) {

    httpSvc.checkIfLoggedIn();

    $scope.inputField = '';

    $scope.addNewRoom = function () {
      httpSvc.addNewRoomToTheBuilding($rootScope.dialogObject, $scope.inputField);
      ngDialog.closeAll(null);
    };

  }); // end of controller
})();