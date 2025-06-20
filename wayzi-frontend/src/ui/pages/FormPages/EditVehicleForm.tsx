import {useDispatch, useSelector} from "react-redux";
import {useEffect, useState} from "react";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {vehicleSchema, VehicleSchemaType} from "../../../schemas/vehicleSchema.ts";
import {
    clearEditVehicleError,
    editVehicle,
    fetchVehicles,
    Vehicle
} from "../../../redux/slices/vehicleSlice.ts";
import {RootState, AppDispatch} from "../../../redux/store.ts";
import {useNavigate, useParams} from "react-router";
import {vehicleColors} from "../../../constants/vehicleColors.ts";
import {vehicleTypes} from "../../../constants/vehicleTypes.ts";
import vehicleRepository from "../../../repository/vehicleRepository.ts";


export const EditVehicleForm = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch<AppDispatch>();

    const {vehicle_id} = useParams();
    const vehicles = useSelector((state: RootState) => state.vehicle.vehicles);
    const existingVehicle = vehicles.find(vehicle => vehicle.id === vehicle_id);
    const [vehicle, setVehicle] = useState<Vehicle | null>(existingVehicle || null);

    const [selectedColor, setSelectedColor] = useState<string | null>(null);
    const [selectedType, setSelectedType] = useState<string | null>(null);

    const {editVehicleError, editVehicleLoading} = useSelector((state: RootState) => state.vehicle)

    useEffect(() => {
        dispatch(clearEditVehicleError());

        return () => {
            dispatch(clearEditVehicleError());
        };
    }, [dispatch]);


    const {register, handleSubmit, setValue, reset, formState: {errors}} = useForm<VehicleSchemaType>({
        resolver: zodResolver(vehicleSchema),
    });

    // Fetch vehicle if not found in store
    useEffect(() => {
        const fetchVehicle = async () => {
            vehicleRepository.findById(String(vehicle_id))
                .then((response) => {
                    setVehicle(response.data);
                })
                .catch((error) => {
                    console.error("Failed to fetch vehicle.", error);
                });
        };

        if (!vehicle && vehicle_id) fetchVehicle();
    }, [vehicle, vehicle_id]);

    // Populate form when vehicle is loaded
    useEffect(() => {
        if (vehicle) {
            setValue("brand", vehicle.brand);
            setValue("model", vehicle.model);
            setValue("color", vehicle.color as VehicleSchemaType["color"]);
            setValue("capacity", vehicle.capacity.toString());
            setValue("type", vehicle.type as VehicleSchemaType["type"]);

            setSelectedColor(vehicle.color);
            setSelectedType(vehicle.type);
        }
    }, [vehicle, setValue]);

    const onSubmit = async (data: VehicleSchemaType) => {
        const resultAction = await dispatch(editVehicle({id: vehicle_id, data}));
        if (!vehicle_id) return;

        if (editVehicle.fulfilled.match(resultAction)) {
            await dispatch(fetchVehicles())
            navigate("/profile")

            reset();
            setSelectedColor(null);
            setSelectedType(null);
        }
    };


    return (
        <div className="custom-container mt-5 page-wrapper">
            <h2 className="text-center mb-4"></h2>
            <form onSubmit={handleSubmit(onSubmit)} className="bg-white p-4 rounded">
                {/* Brand and Model */}
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label className="form-label">Brand</label>
                        <input type="text" {...register("brand")} className="form-control"/>
                        {errors.brand && <p className="text-danger">{errors.brand.message}</p>}
                    </div>
                    <div className="col-md-6">
                        <label className="form-label">Model</label>
                        <input type="text" {...register("model")} className="form-control"/>
                        {errors.model && <p className="text-danger">{errors.model.message}</p>}
                    </div>
                </div>

                {/* Color and Capacity */}
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label className="form-label">Select Color</label>
                        <div className="d-flex flex-wrap gap-2">
                            {vehicleColors.map(color => (
                                <button
                                    key={color.name}
                                    type="button"
                                    className={`color-btn ${color.className} ${selectedColor === color.name ? "active" : ""}`}
                                    style={color.style}
                                    onClick={() => {
                                        setSelectedColor(color.name);
                                        setValue("color", color.name as VehicleSchemaType["color"]);
                                    }}
                                ></button>
                            ))}
                        </div>
                        {errors.color && <p className="text-danger">{errors.color.message}</p>}
                    </div>

                    <div className="col-md-6">
                        <label className="form-label">Capacity</label>
                        <input type="number" {...register("capacity")} className="form-control"/>
                        {errors.capacity && <p className="text-danger">{errors.capacity.message}</p>}
                    </div>
                </div>

                {/* Vehicle Type */}
                <div className="mb-3">
                    <label className="form-label d-block">Vehicle Type</label>
                    <div className="row">
                        {vehicleTypes.map(type => (
                            <div key={type.name} className="col-12 col-sm-6 col-md-3 mb-3">
                                <button
                                    type="button"
                                    className={`radio-btn w-100 text-center ${selectedType === type.name ? "active" : ""}`}
                                    onClick={() => {
                                        setSelectedType(type.name);
                                        setValue("type", type.name as VehicleSchemaType["type"]);
                                    }}
                                >
                                    <i className={`fa-solid ${type.icon}`}></i>
                                    <h5 className="card-title">{type.label}</h5>
                                </button>
                            </div>
                        ))}
                    </div>
                    {errors.type && <p className="text-danger">{errors.type.message}</p>}
                </div>

                {/* Submit Buttons */}
                <div className="d-flex justify-content-end">
                    <button type="button" className="btn btn-light ms-2" onClick={() => reset()}>
                        Cancel
                    </button>
                    <button
                        type="submit"
                        className={`btn custom-btn ms-2 ${(editVehicleLoading) ? "opacity-50 cursor-not-allowed" : ""}`}
                        disabled={(editVehicleLoading)}
                    >
                        {(editVehicleLoading) ? "Submitting..." : "Save"}
                    </button>
                </div>

                {editVehicleError && <p className="text-danger mt-3">{editVehicleError}</p>}
            </form>
        </div>
    );
};
