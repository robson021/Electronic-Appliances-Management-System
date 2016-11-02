(function () {
    "use strict";
    angular.module("ngApp").controller('user-panel-ctrl', function ($scope, $rootScope, $state, httpSvc) {

        httpSvc.checkIfLoggedIn();

    }); // end of controller
})();