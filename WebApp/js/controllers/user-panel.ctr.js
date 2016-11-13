(function () {
    "use strict";
    angular.module("ngApp").controller('user-panel-ctrl', function ($scope, $rootScope, $state, httpSvc, ngDialog) {

        httpSvc.checkIfLoggedIn();
        httpSvc.checkIfAdmin();

        const self = this;
        const possibleViews = ['buildings', 'rooms', 'appliances'];

        $scope.currentView = null;

        $scope.allBuildings = null;
        $scope.allRoomsInBuilding = null;
        $scope.allAppliancesInRoom = null;

        $scope.selectedBuilding = null;
        $scope.selectedRoom = null;
        $scope.selectedAppliance = null;

        $scope.loadBuildings = function () {
            httpSvc.getAllBuildings()
                .success(function (response) {
                    $scope.allBuildings = response;
                    $scope.currentView = possibleViews[0];
                    console.info('get all buildings: ' + $scope.allBuildings);
                });
        };

        $scope.loadRooms = function () {
            if ($scope.selectedBuilding == null) {
                toastr.info('Select building first.');
                return;
            }
            httpSvc.getAllRoomsInBuilding($scope.selectedBuilding)
                .success(function (response) {
                    $scope.allRoomsInBuilding = response;
                    $scope.currentView = possibleViews[1];
                    console.info('get all rooms: ' + $scope.allRoomsInBuilding.length);
                });
        };

        $scope.loadAppliances = function () {
            if ($scope.selectedRoom == null) {
                toastr.info('Select room first.');
                return;
            }
            httpSvc.getAllAppliancesInRoom($scope.selectedRoom.id)
                .success(function (response) {
                    $scope.allAppliancesInRoom = response;
                    $scope.currentView = possibleViews[2];
                    console.info('get all appliances: ' + $scope.allAppliancesInRoom.length);
                });
        };

        $scope.selectBuilding = function (b) {
            $scope.selectedBuilding = b;
            $scope.loadRooms();
            console.info("Current selected building: " + $scope.selectedBuilding);
        };

        $scope.selectRoom = function (r) {
            $scope.selectedRoom = r;
            $scope.loadAppliances();
            console.info("Current selected room: " + $scope.selectedRoom.number);
        };

        $scope.selectAppliance = function (a) {
            $scope.selectedAppliance = a;
            console.info("Current selected appliance: " + $scope.selectedAppliance.name);
            // todo
        };

        $scope.editBuilding = function (b) {
            $rootScope.dialogObject = b;
            ngDialog.open({
                template: 'dialogs/templates/editBuilding.html',
                controller: 'edit-buildings-ctrl'
            });
        };

        $scope.editRoom = function (r) {
            $rootScope.dialogObject = r;
            ngDialog.open({
                template: 'dialogs/templates/editRoom.html',
                controller: 'edit-room-ctrl'
            });
        };

        $scope.editAppliance = function (a) {
            $rootScope.dialogObject = a;
            ngDialog.open({
                template: 'dialogs/templates/editAppliance.html',
                controller: 'edit-appliance-ctrl'
            });
        };

        $scope.addNewAppliance = function () {
            $rootScope.dialogObject = $scope.selectedRoom;
            ngDialog.open({
                template: 'dialogs/templates/newAppliance.html',
                controller: 'add-appliance-ctrl'
            });
        };

    }); // end of controller
})();