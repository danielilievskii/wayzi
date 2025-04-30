import {Ride} from "../../redux/slices/rideSlice.ts";
import {useMap} from "react-leaflet";
import {useEffect} from "react";
import L from 'leaflet';
import axiosInstance from "../../axios/axiosInstance.ts";

export default function MapContent({ride} : {ride: Ride}) {
    const map = useMap();

    useEffect(() => {
        if(!ride) return;

        const coordinates: [number, number][] = [];

        const start: [number, number] = [
            ride.departureLocation.latitude,
            ride.departureLocation.longitude,
        ];
        const end: [number, number] = [
            ride.arrivalLocation.latitude,
            ride.arrivalLocation.longitude,
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


        L.marker(end)
            .addTo(map)
            .bindPopup(`Arrival: ${ride.arrivalLocation.displayName}`);

        coordinates.push(end);

        // map.fitBounds(coordinates);

        const fetchRoutes = async () => {
            for (let i = 0; i < coordinates.length - 1; i++) {
                const [startLat, startLng] = coordinates[i];
                const [endLat, endLng] = coordinates[i + 1];
                try {
                    const res = await axiosInstance.get(`/routes/directions`, {
                        params: { startLat, startLng, endLat, endLng },
                    });

                    const routeCoords = res.data.features[0].geometry.coordinates.map(
                        ([lng, lat]: [number, number]) => [lat, lng]
                    );
                    L.polyline(routeCoords, {
                        color: "#08C2FF",
                        weight: 5,
                    }).addTo(map);
                } catch (e) {
                    console.error("Failed to fetch route", e);
                }
            }
        };

        fetchRoutes()
    }, [ride, map]);

    return null;
}