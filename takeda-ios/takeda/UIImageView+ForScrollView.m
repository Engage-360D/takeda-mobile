
@implementation UIImageView (ForScrollView)

-(void)setAlpha:(CGFloat)alpha{
    if (self.superview.tag == noDisableVerticalScrollTag) {
        alpha = 1.0f;
    }
    [super setAlpha:alpha];

}

//- (void) setAlpha:(float)alpha {
//
//    if (self.superview.tag == noDisableVerticalScrollTag) {
//        super.alpha = 1.0f;
//    }
//
//    
//    
//    if (self.superview.tag == noDisableVerticalScrollTag) {
//        if (alpha == 0 && self.autoresizingMask == UIViewAutoresizingFlexibleLeftMargin) {
//            if (self.frame.size.width < 10 && self.frame.size.height > self.frame.size.width) {
//                UIScrollView *sc = (UIScrollView*)self.superview;
//                if (sc.frame.size.height < sc.contentSize.height) {
//                    return;
//                }
//            }
//        }
//    }
//    
//    if (self.superview.tag == noDisableHorizontalScrollTag) {
//        if (alpha == 0 && self.autoresizingMask == UIViewAutoresizingFlexibleTopMargin) {
//            if (self.frame.size.height < 10 && self.frame.size.height < self.frame.size.width) {
//                UIScrollView *sc = (UIScrollView*)self.superview;
//                if (sc.frame.size.width < sc.contentSize.width) {
//                    return;
//                }
//            }
//        }
//    }
//    
//    [super setAlpha:alpha];
//}



@end
