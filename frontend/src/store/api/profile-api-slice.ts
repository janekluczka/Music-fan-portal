import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { UserProfileImage } from "../../components/Blog/BlogPostPage/PostComments/PostComments";
import {
  ProfileEditType,
  ProfileType,
} from "../../components/Profile/Edit/ProfileEdit";
import { RootState } from "../index";
import { UserProfileDTO } from "./result/dto/activity/UserProfileDTO";
import { UserRepresentation } from "./result/dto/activity/UserRepresentation";

const baseQuery = fetchBaseQuery({
  baseUrl: "http://localhost:4561",
  credentials: "include",
  prepareHeaders: (headers, { getState }) => {
    const token = (getState() as RootState).auth.idToken;
    if (token) {
      headers.set("Authorization", `Bearer ${token}`);
    }
    return headers;
  },
});

const profileApiSlice = createApi({
  reducerPath: "profileApi",
  baseQuery: baseQuery,
  tagTypes: ["Observed", "Observers"],
  endpoints: (builder) => ({
    updateProfile: builder.mutation<void, ProfileEditType>({
      query: (body) => ({
        url: `/profile`,
        method: "PUT",
        body,
      }),
    }),
    getProfile: builder.query<ProfileType, void>({
      query: () => ({
        url: `/profile`,
      }),
    }),
    getUserDetails: builder.query<UserRepresentation, string>({
      query: (username) => ({
        url: `/profile/details/${username}`,
      }),
    }),
    getUsersImage: builder.query<UserProfileImage[], string>({
      query: (params) => ({
        url: `/profile/image?usernames=${params}`,
      }),
    }),
    getObservers: builder.query<UserProfileDTO[], string>({
      query: (username) => ({
        url: `/profile/${username}/observers`,
      }),
      providesTags: (result, error, arg) => result ? [({ type: "Observers" as const, arg}), "Observers"] : ["Observers"],
    }),
    getObserving: builder.query<UserProfileDTO[], string>({
      query: (username) => ({
        url: `/profile/${username}/observing`,
      }),
      providesTags: (result, error, arg) => result ? [({ type: "Observed" as const, arg}), "Observed"] : ["Observed"],
    }),
    changeObservationStatus: builder.mutation<void, {observer: string, observed: string}>({
      query: ({observer, observed}) => ({
        url: `/profile/observation?observed=${observed}&observing=${observer}`,
        method: "POST",
      }),
      invalidatesTags: (result, error, arg) => result ? [{ type: "Observed", id: arg.observer}, {type: "Observers", id: arg.observed}] : ["Observed", "Observers"],
    }),
  }),
});

export const {
  useGetObserversQuery,
  useGetObservingQuery,
  useUpdateProfileMutation,
  useLazyGetUserDetailsQuery,
  useLazyGetProfileQuery,
  useLazyGetUsersImageQuery,
  useChangeObservationStatusMutation,
} = profileApiSlice;
export default profileApiSlice;