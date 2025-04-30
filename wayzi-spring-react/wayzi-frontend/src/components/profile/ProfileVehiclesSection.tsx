import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../redux/store.ts";
import {useEffect} from "react";
import {deleteVehicle, fetchVehicles, Vehicle} from "../../redux/slices/vehicleSlice.ts";
import { Link } from "react-router";
import {useAsyncThunkHandler} from "../../hooks/useAsyncThunkHandler.ts";
import {getVehicleIcon} from "../../utils/vehicleUtils.ts";

export const ProfileVehiclesSection = () => {
    const dispatch = useDispatch<AppDispatch>()
    const vehicles = useSelector((state: RootState) => state.vehicle.vehicles)

    const { handleThunk, loading, success, error } = useAsyncThunkHandler();

    useEffect(() => {
        if(vehicles.length == 0) {
            dispatch(fetchVehicles());
        }

    }, [dispatch]);


    const onDelete = (id: number) => {

        console.log(id)
        handleThunk(dispatch, deleteVehicle, id, () => {

            // TODO: Success notification
        })
    }


    return (
        <div className="floating-card shadow-sm p-3">
            <h5 className="mb-3">Vehicles</h5>
            <div className="rounded border-dashed border-gray-500 p-3 mb-2 text-center cursor-pointer">
                <Link className="text-reset text-decoration-none" to={`/vehicles/add`}> + Add vehicle </Link>
            </div>

            {vehicles?.map((vehicle: Vehicle) => (
                <div key={vehicle.id} className="rounded bg-light p-3 mb-2">
                    <div className="d-flex justify-content-between">
                        <div>
                            <i className={`fa-solid ${getVehicleIcon(vehicle.type)} mx-3`}></i>
                            <span>{vehicle.brand} </span>
                            <span>{vehicle.model} </span>
                            <span>- {vehicle.color} </span>
                        </div>
                        <div className="d-flex gap-3">
                            <Link className="text-reset text-decoration-none " to={`/vehicles/edit/${vehicle.id}`}>
                                <i className="fa-solid fa-pen-to-square action-button"></i>
                            </Link>
                            <button type="submit"
                                    className="action-button"
                                    onClick={() => onDelete(vehicle.id)}>
                                <i className="fa-solid fa-trash"></i>
                            </button>
                        </div>
                    </div>
                </div>
            ))}

            {error && <p>${error}</p>}
        </div>
    )
}