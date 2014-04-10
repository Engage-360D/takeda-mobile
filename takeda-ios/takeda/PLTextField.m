//
//  PLTextField.m
//

#import "PLTextField.h"

@implementation PLTextField
@synthesize placeholderColor;
@synthesize placeholderFont;


- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}


- (void)drawPlaceholderInRect:(CGRect)rect{
    
    if (!placeholderColor) {
        placeholderColor = RGBA(255, 255, 255, 1.0);
    }
    if (!placeholderFont) {
        placeholderFont = self.font;
    }
    
    
    
    
    if (self.placeholder){
        if (IOS7_AND_LATER) {
            CGSize drawSize = [self.placeholder sizeWithAttributes:[NSDictionary dictionaryWithObject:self.font forKey:NSFontAttributeName]];
            CGRect drawRect = rect;
            drawRect.origin.y = (rect.size.height - drawSize.height) * 0.5 - 0.5;
            NSDictionary *drawAttributes = @{NSFontAttributeName: placeholderFont,
                                             NSForegroundColorAttributeName : placeholderColor};
            
            // draw
            [self.placeholder drawInRect:drawRect withAttributes:drawAttributes];
        }else{
#warning unkoment if will be <ios7
            
            [[UIColor redColor] setFill];
            [placeholderColor setFill];
            rect.origin.y = rect.origin.y + 1;
            [[self placeholder] drawInRect:rect withFont:placeholderFont];
            
        }
    }
    
}

@end
