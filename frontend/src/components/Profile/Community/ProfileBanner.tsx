import { Box, useBreakpointValue, Button, Icon, VStack, HStack, Text, useToken, Spacer, useToast } from "@chakra-ui/react";
import { motion } from "framer-motion";
import { useEffect, useState } from "react";
import { AiFillCamera } from "react-icons/ai";
import { BsCardImage } from "react-icons/bs";
import { useSelector } from "react-redux";
import { RootState } from "../../../store";
import { useGetCommentStatisticsQuery } from "../../../store/api/productSlice";
import { isCommentStatistics } from "../../../store/api/result/dto/product/CommentStatistics";
import { UserDetails } from "../../../utils/KeycloakUtils";
import { AiOutlineUser } from "react-icons/ai";
import { useChangeObservationStatusMutation } from "../../../store/api/profile-api-slice";
import { UserProfileDTO } from "../../../store/api/result/dto/activity/UserProfileDTO";

export interface ProfileBannerProps {
    bannerImage?: string,
    profileImage: string,
    userDetails: UserDetails,
    observers: UserProfileDTO[],
    observed: UserProfileDTO[],
};

const ProfileBanner: React.FC<ProfileBannerProps> = ({ bannerImage, userDetails, observers, observed }) => {
    const initialStatistics: [string, number][]  = [
        ["Total Upvotes", 0],
        ["Posts", 0],
        ["Comments", 0],
        ["Followers", observers.length],
        ["Following", observed.length],
    ];
    const currentUsername = useSelector((state: RootState) => state.auth.userDetails?.personal.username ?? "");
    const [isObserving, setIsObserving] = useState<boolean>(true);
    const [statistics, setStatistics] = useState<Map<string, number>>(new Map(initialStatistics));
    const [errorMessages, setErrorMessages] = useState<string[]>([]);
    const [primary400, primary600] = useToken("colors", ["primary.400", "primary.600"]);
    const { data: commentStatisticsData } = useGetCommentStatisticsQuery(userDetails.personal.username, { refetchOnMountOrArgChange: true});
    const genericStatisticsErrorMessage = "Could not set statistics from our service";
    const toast = useToast();
    const [changeObservationStatusTrigger] = useChangeObservationStatusMutation();
    
    const bannerHeight = useBreakpointValue({
        base: "225px",
        md: "250px",
        lg: "275px",
        xl: "300px",
        '2xl': "325px",
    })!;

    const headerSize = useBreakpointValue({
        base: 24,
        xl: 32,
        '2xl': 36,
    })!;

    useEffect(() => {
        setIsObserving(observers.some(observer => observer.username === currentUsername));
    }, [currentUsername, observers]);

    useEffect(() => {
        if (commentStatisticsData) {
            if (isCommentStatistics(commentStatisticsData)) {
                setStatistics((statistics) => {
                    statistics.set("Total Upvotes", commentStatisticsData.upvoteCount);
                    statistics.set("Comments", commentStatisticsData.commentsCount);

                    return statistics;
                })
            } else {
                setErrorMessages([genericStatisticsErrorMessage]);
            }
        }
    }, [commentStatisticsData]);

    useEffect(() => {
        errorMessages.forEach(message => toast({
          title: "Something went wrong",
          description: message,
          status: "error",
          duration: 9000,
          isClosable: true,
        }))
      }, [errorMessages]);
    
    useEffect(() => {
        setStatistics((currentState) => {
            currentState.set("Following", observed.length);
            currentState.set("Followers", observers.length);
            
            return currentState;
        });
    }, [observed, observers]);

    const changeObservationStatus = () => {
        changeObservationStatusTrigger({observer: currentUsername, observed: userDetails.personal.username})
        .then(() => setIsObserving(!isObserving));
    };
    
    const statisticsArray = Array.from(statistics);
    
    return <Box w="100%">
        <Box backgroundImage={bannerImage} backgroundPosition="center" backgroundRepeat="no-repeat" h={bannerHeight} w="100%" position="relative" boxShadow="0 4px 4px 0 rgba(0,0,0,0.25)">
            <VStack w="100%" h="inherit" justifyContent="center" backgroundColor={ bannerImage ? "rgba(0,0,0,0.35)" : ""}>
                {userDetails.personal.username === currentUsername ? (bannerImage ?
                <Button bg="primary.300" _hover={{bg: "primary.400"}} _active={{bg: "primary.400"}} color="white" leftIcon={<Icon as={AiFillCamera}/>} fontSize="14px" h="32px" px="8px" mt="24px">
                    Edit
                </Button>
                : <HStack as={motion.div} alignSelf="end" px="16px" py="8px" userSelect="none" cursor="pointer" color={primary400} initial={{color: primary400}} whileHover={{color: primary600 }}>
                    <Text fontSize="18px" textStyle="p" fontWeight={500}>+ Upload Cover Photo</Text>  
                    <Icon as={BsCardImage} boxSize="18px" />
                  </HStack>) : <Button bg="primary.300" _hover={{bg: "primary.400"}} _active={{bg: "primary.400"}} color="white" leftIcon={<Icon as={AiOutlineUser} />} fontSize="14px" h="32px" px="8px" mt="24px"
                  onClick={changeObservationStatus}>{isObserving ? "Unfollow" : "Follow"}</Button>}
                <Spacer />  
                <Text textStyle="h3" color="primary.400" fontSize={headerSize + "px"}>{userDetails ? userDetails.personal.username : "Unknown"}</Text>  
                <Text textStyle="h3" color="primary.300" fontSize={(headerSize - 4) + "px"}>{userDetails ? `${userDetails.personal.name} ${userDetails.personal.surname}` : "Unknown" }</Text>    
                <HStack justifyContent="center" pb="4px" spacing={{base: "16px", md: "24px"}} userSelect="none" cursor="pointer">
                    {statisticsArray.map((entry, index) => <Statistic key={index} count={entry[1]} label={entry[0]} />)}
                </HStack>  
            </VStack>  
        </Box>
    </Box>
}

export interface StatisticProps {
    count: number,
    label: string,
}

const Statistic: React.FC<StatisticProps> = ({count, label}) => {
    const headerSize = useBreakpointValue({
        base: 20,
        xl: 24,
        '2xl': 32,
    })!;

    const fontSize = useBreakpointValue({
        base: 16,
        xl: 20,
        '2xl': 24,
    });

    return (
        <VStack spacing={0}>
            <Text textStyle="p" color="gray.200" fontSize={headerSize + "px"}>{count}</Text>
            <Text textStyle="p" color="gray.200" fontSize={fontSize + "px"}>{label}</Text>
        </VStack>
    )
}

export default ProfileBanner;