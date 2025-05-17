import {useMap} from "react-leaflet";
import {useEffect} from "react";
import L from 'leaflet';
import {RideDetails} from "../../../../redux/slices/rideDetailsSlice.ts";
import rideRepository from "../../../../repository/rideRepository.ts";

export default function MapContent({ride}: { ride: RideDetails }) {
    const map = useMap();

    useEffect(() => {
        if (!ride) return;

        const coordinates: [number, number][] = [];

        const start: [number, number] = [
            ride.departureLocation.latitude,
            ride.departureLocation.longitude,
        ];

        L.marker(start)
            .addTo(map)
            .bindPopup(`Departure: ${ride.departureLocation.displayName}`);

        coordinates.push(start);

        ride.rideStops.forEach((stop) => {
            const stopCoords: [number, number] = [
                stop.location.latitude,
                stop.location.longitude,
            ];
            L.marker(stopCoords)
                .addTo(map)
                .bindPopup(`Stop: ${stop.location.displayName}`);
            coordinates.push(stopCoords);
        });

        const end: [number, number] = [
            ride.arrivalLocation.latitude,
            ride.arrivalLocation.longitude,
        ];
        L.marker(end)
            .addTo(map)
            .bindPopup(`Arrival: ${ride.arrivalLocation.displayName}`);

        coordinates.push(end);
        map.fitBounds(coordinates);


        const fetchRoute = async () => {
            try {
                const response = await rideRepository.findRouteCoordinatesById(ride.id);
                const route = response.data;

                L.polyline(route, {
                    color: "#08C2FF",
                    weight: 5,
                }).addTo(map);

            } catch (error) {
                console.error("Failed to fetch and render route:", error);
            }
        };

        fetchRoute()
    }, [map]);

    return null;
}