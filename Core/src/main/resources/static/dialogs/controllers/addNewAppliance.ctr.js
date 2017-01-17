(function () {
    "use strict";
    angular.module("ngApp").controller('add-appliance-ctrl', function ($scope, $rootScope, ngDialog, httpSvc) {

        httpSvc.checkIfLoggedIn();

        $scope.inputField = '';

        $scope.addNewAppliance = function () {
            httpSvc.addNewApplianceToTheRoom($rootScope.dialogObject.id, $scope.inputField);
            ngDialog.closeAll(null);
        };

    }); // end of controller
})();