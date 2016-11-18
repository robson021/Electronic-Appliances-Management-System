(function () {
    "use strict";
    angular.module("ngApp").controller('my-reservations-ctrl', function ($scope, httpSvc) {

        httpSvc.checkIfLoggedIn();

        $scope.myReservations = null;

        httpSvc.getMyReservations()
            .success(function (response) {
               $scope.myReservations = $scope.convertReservations(response);
            });

        $scope.convertReservations = function(reservations) {
            if (reservations.length <= 0) {return;}
            let res = [];
            reservations.forEach(function (r) {
                let reservation = {};
                reservation.id = r.id;
                reservation.from = new Date(r.from);
                reservation.minutes = r.minutes;
                res.push(reservation);
            });
            return res;
        }

    }); // end of controller
})();