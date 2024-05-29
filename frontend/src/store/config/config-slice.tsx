import { createSlice } from "@reduxjs/toolkit";

const configInititalState = { serverUrl: "http://192.168.195.23:4561" };

const configSlice = createSlice({
  name: "config",
  initialState: configInititalState,
  reducers: {},
});

export default configSlice.reducer;