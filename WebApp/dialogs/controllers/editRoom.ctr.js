(function () {
    "use strict";
    angular.module("ngApp").controller('edit-room-ctrl', function ($scope, $rootScope, ngDialog, httpSvc) {

        httpSvc.checkIfLoggedIn();

        $scope.inputField = '';

        $scope.deleteRoom = function () {
            httpSvc.deleteExistingRoom($rootScope.dialogObject.id);
            ngDialog.closeAll(null);
        };

        $scope.renameRoom = function () {
            httpSvc.renameRoom($rootScope.dialogObject.id, $scope.inputField);
            ngDialog.closeAll(null);
        };

    }); // end of controller
})();