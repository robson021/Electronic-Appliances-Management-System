(function () {
  "use strict";
  angular.module("ngApp").controller('edit-buildings-ctrl', function ($scope, $rootScope, ngDialog, httpSvc) {

    httpSvc.checkIfLoggedIn();

    $scope.inputField = '';

    $scope.deleteBuilding = function () {
      httpSvc.deleteExistingBuilding($rootScope.dialogObject);
      ngDialog.closeAll(null);
    };

    $scope.renameBuilding = function () {
      httpSvc.renameBuilding($rootScope.dialogObject, $scope.inputField);
      ngDialog.closeAll(null);
    };

  }); // end of controller
})();