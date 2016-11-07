(function () {
    "use strict";
    angular.module("ngApp").controller('edit-appliance-ctrl', function ($scope, $rootScope, ngDialog, httpSvc) {

        httpSvc.checkIfLoggedIn();

        $scope.inputField = '';

        $scope.deleteAppliance = function () {
            httpSvc.deleteExistingAppliance($rootScope.dialogObject.id);
            ngDialog.closeAll(null);
        };

        $scope.renameAppliance = function () {
            httpSvc.renameAppliance($rootScope.dialogObject.id, $scope.inputField);
            ngDialog.closeAll(null);
        };

    }); // end of controller
})();