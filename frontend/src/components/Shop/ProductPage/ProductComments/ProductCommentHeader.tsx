import { HStack, Icon, Spacer, Text, Avatar } from "@chakra-ui/react";
import { DateTime } from "luxon";
import { FaUserCircle } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { CommentOutputDTO } from "../../../../store/api/result/dto/product/CommentOutputDTO";

type ProductCommentHeaderProps = {
  comment: CommentOutputDTO,
  avatarUrl?: string,
};

const ProductCommentHeader = ({ comment, avatarUrl }: ProductCommentHeaderProps) => {
  const navigate = useNavigate();
  const avatarSize = {
    base: "24px",
    sm: "28px",
    md: "32px",
    lg: "36px",
    xl: "40px",
  };
  
  return (
    <HStack width="95%">
      {avatarUrl ? <Avatar src={avatarUrl} boxSize={avatarSize} onClick={() => navigate(`/profile/community/${comment.username}`)} cursor="pointer"/> :
      <Icon
        boxSize={avatarSize}
        as={FaUserCircle}
        onClick={() => navigate(`/profile/community/${comment.username}`)}
        cursor="pointer"
      />}
      <Text
        pl="1%"
        fontSize={{
          base: "12px",
          sm: "14px",
          md: "16px",
          lg: "18px",
          xl: "20px",
        }}
        textStyle="p"
      >
        {comment.username}
      </Text>
      <Spacer />
      <Text
        color="gray"
        fontSize={{
          base: "9px",
          sm: "10px",
          md: "11px",
          lg: "12px",
          xl: "13px",
        }}
        textStyle="p"
      >
        {DateTime.fromISO(comment.creationTimestamp).toLocaleString(DateTime.DATETIME_MED)}
      </Text>
    </HStack>
  );
};

export default ProductCommentHeader;
