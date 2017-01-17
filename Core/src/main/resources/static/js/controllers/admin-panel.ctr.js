(function () {
  "use strict";
  angular.module("ngApp").controller('admin-panel-ctrl', function ($scope, $rootScope, httpSvc) {

    httpSvc.checkIfLoggedAsAdmin();

    const self = this;
    const possibleViews = [ 'activatedAcc', 'deactivatedAcc' ];

    $scope.currentView = null;

    $scope.users = null;

    $scope.loadActivatedUsers = function () {
      httpSvc.getAllActivatedAccounts()
        .success(function (response) {
          $scope.users = response;
        });
      $scope.currentView = possibleViews[ 0 ];
    };

    $scope.loadInactivatedUsers = function () {
      httpSvc.getAllInactiveAccounts()
        .success(function (response) {
          $scope.users = response;
        });
      $scope.currentView = possibleViews[ 1 ];
    };

    $scope.deactivateUser = function (user) {
      httpSvc.deactivateUser(user.email);
    };

    $scope.activateUser = function (user) {
      httpSvc.activateUser(user.email);
    };

    $scope.deleteUser = function (user) {
      httpSvc.deleteUser(user.email);
    };

  }); // end of controller
})();