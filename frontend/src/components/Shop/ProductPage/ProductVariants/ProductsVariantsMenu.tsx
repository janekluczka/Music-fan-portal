import { HStack, Text, useBreakpointValue, useRadioGroup, VStack } from "@chakra-ui/react";
import { Dispatch, SetStateAction } from "react"
import { imageHeight } from "../ProductBasicInfo/ProductDimensions";
import RadioCard from "../ProductBasicInfo/RadioCard";

export interface VariantsGroup {
    name: string,
    options: string[],
    isDisabled: (option: string) => boolean,
    selectedOption: string
    setSelectedOption: Dispatch<SetStateAction<string>>,
}


export interface ProductsVariantsMenuProps {
    variantsGroups: VariantsGroup[],
}

const ProductsVariantsMenu: React.FC<ProductsVariantsMenuProps> = ({ variantsGroups }) => {
    const imageWidth = parseInt(useBreakpointValue(imageHeight)!) * 1.5;
    const width = {
        base: imageWidth,
        lg: 320 + imageWidth,
        xl: 440 + imageWidth,
        "2xl": 560 + imageWidth,
    };

    return <VStack spacing="2px" mt="0px !important" w={width}>
        {variantsGroups.map(variantGroup => <ProductsVariantsStack key={variantGroup.name} variantGroup={variantGroup} />)}
    </VStack>
}

export interface ProductsVariantsStackProps {
    variantGroup: VariantsGroup,
} 

const ProductsVariantsStack: React.FC<ProductsVariantsStackProps> = ({variantGroup}) => {
    const { getRootProps, getRadioProps } = useRadioGroup({
        name: variantGroup.name,
        defaultValue: variantGroup.options[0],
        onChange: variantGroup.setSelectedOption,
    });

    const group = getRootProps();

    return (
        <HStack {...group} alignSelf="start">
            <Text color="primary.300" fontWeight="bold" fontSize={{base: "12px", sm: "14px", md: "16px", lg: "18px"}}>{variantGroup.name}</Text>
            {variantGroup.options.map((value) => {
                const radio = {...getRadioProps({ value }), isDisabled: variantGroup.isDisabled(value)};

                console.log(radio);

                return <RadioCard key={value} {...radio}>
                    {value}
                </RadioCard>
            })}
        </HStack>
    )
}

export default ProductsVariantsMenu;