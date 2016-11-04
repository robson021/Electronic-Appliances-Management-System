(function () {
    "use strict";
    angular.module("ngApp").controller('admin-panel-ctrl', function ($scope, $rootScope, $state, httpSvc) {

        httpSvc.checkIfLoggedIn();

    }); // end of controller
})();