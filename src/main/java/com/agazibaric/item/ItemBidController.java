package com.agazibaric.item;

import com.agazibaric.user.User;
import com.agazibaric.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class ItemBidController {

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ImageService imageService;

    @PostMapping("items/{id}/image")
    public void handleImagePost(@PathVariable String id, @RequestParam("imagefile") MultipartFile file){
        imageService.saveImageFile(Long.valueOf(id), file);
        System.out.println("Image saved");
    }

    @GetMapping("/items/{id}/image")
    public ResponseEntity<Resource> getImageFile(@PathVariable String id) {
        Resource image = imageService.loadItemImage(Long.valueOf(id));
        if (image == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + image.getFilename() + "\"")
                .body(image);
    }

    @RequestMapping(value = "/bid", method = RequestMethod.GET)
    public ResponseEntity makeABid(@RequestParam(value = "itemId") Long itemId,
                                   @RequestParam(value = "bid") Float bid, Principal principal) {

        if (itemId == null || bid == null || bid <= 0.0f)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        Optional<Item> op = itemRepo.findById(itemId);
        if (!op.isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bid is made on nonexisting item.");

        Item item = op.get();
        User loggedInUser = userRepo.findByUsername(principal.getName());
        if (item.getUser().equals(loggedInUser))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User can not bid on his item.");

        if (ItemUtil.isItemExpired(item))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item time has expired.");

        Float currentBid = item.getBidPrice();
        if (currentBid == null || currentBid < bid) {
            item.setBidPrice(bid);
            item.setHighestBidder(loggedInUser);
            item.setNumberOfBids(item.getNumberOfBids() + 1);
            itemRepo.save(item);
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Bid amount has to be greater then current bid amount which is " + item.getBidPrice());
    }


}
