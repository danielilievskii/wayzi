import { useDispatch } from "react-redux";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { vehicleSchema, VehicleSchemaType } from "../../../schemas/vehicleSchema.ts";
import { createVehicle } from "../../../redux/slices/vehicleSlice.ts";
import { AppDispatch } from "../../../redux/store.ts";
import {useAsyncThunkHandler} from "../../../hooks/useAsyncThunkHandler.ts";
import {useNavigate} from "react-router";

const colors = [
    { name: "RED", className: "bg-danger" },
    { name: "BLUE", className: "bg-primary" },
    { name: "GREEN", className: "bg-success" },
    { name: "YELLOW", className: "bg-warning" },
    { name: "BLACK", className: "bg-black text-white" },
    { name: "WHITE", className: "bg-white border-light" },
    { name: "ORANGE", className: "bg-orange", style: { backgroundColor: "orange" } },
    { name: "PURPLE", className: "bg-purple", style: { backgroundColor: "purple" } },
    { name: "PINK", className: "bg-pink", style: { backgroundColor: "pink" } },
    { name: "BROWN", className: "bg-brown", style: { backgroundColor: "brown" } },
    { name: "GREY", className: "bg-secondary" },
];

const vehicleTypes = [
    { name: "AUTOMOBILE", icon: "fa-car", label: "Automobile" },
    { name: "MOTORBIKE", icon: "fa-motorcycle", label: "Motorbike" },
    { name: "BUS", icon: "fa-bus", label: "Bus" },
    { name: "VAN", icon: "fa-van-shuttle", label: "Van" },
];

export const AddVehicleForm = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch<AppDispatch>();
    // const loading = useSelector((state: RootState) => state.profile.loading);

    const [selectedColor, setSelectedColor] = useState<string | null>(null);
    const [selectedType, setSelectedType] = useState<string | null>(null);

    const {register, handleSubmit, setValue, reset, formState: { errors } } = useForm<VehicleSchemaType>({
        resolver: zodResolver(vehicleSchema),
    });

    const { handleThunk, loading, success, error } = useAsyncThunkHandler();


    const onSubmit = (data: VehicleSchemaType) => {
        handleThunk(dispatch, createVehicle, data, () => {
            reset();
            setSelectedColor(null);
            setSelectedType(null);

            navigate("/profile")
        });
    };

    return (
        <div className="custom-container mt-5 page-wrapper">
            <h2 className="text-center mb-4"></h2>
            <form onSubmit={handleSubmit(onSubmit)} className="bg-white p-4 rounded">
                {/* Brand and Model */}
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label className="form-label">Brand</label>
                        <input type="text" {...register("brand")} className="form-control" />
                        {errors.brand && <p className="text-danger">{errors.brand.message}</p>}
                    </div>
                    <div className="col-md-6">
                        <label className="form-label">Model</label>
                        <input type="text" {...register("model")} className="form-control" />
                        {errors.model && <p className="text-danger">{errors.model.message}</p>}
                    </div>
                </div>

                {/* Color and Capacity */}
                <div className="row mb-3">
                    <div className="col-md-6">
                        <label className="form-label">Select Color</label>
                        <div className="d-flex flex-wrap gap-2">
                            {colors.map(color => (
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
                        <input type="number" {...register("capacity")} className="form-control" />
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
                    <button type="button" className="btn btn-light me-2" onClick={() => reset()}>
                        Cancel
                    </button>
                    <button
                        type="submit"
                        className={`btn custom-btn ml-2 ${(loading && (success || error))? "opacity-50 cursor-not-allowed" : ""}`}
                        disabled={(loading && (success || error))}
                    >
                        {(loading && (success || error)) ? "Submitting..." : "Save"}
                    </button>
                </div>

                {success && <p className="text-success mt-3">Vehicle added successfully!</p>}
                {error && <p className="text-danger mt-3">{error}</p>}
            </form>
        </div>
    );
};
