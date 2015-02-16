//
//  UITextView+Placeholder.m
//  iMedicum
//
//  Created by Alexander Rudenko on 28.10.14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import "UITextView+Placeholder.h"
#import <objc/runtime.h>


static char const * const placeholderKey = "placeholderKey";

@implementation UITextView (Placeholder)
@dynamic placeholder;

-(void)setupPlaceholder:(NSString*)text withFont:(UIFont*)font{
    if (!self.placeholder){
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(textChanged:) name:UITextViewTextDidChangeNotification object:self];
        
        self.placeholder = [[UITextView alloc] initWithFrame:CGRectMake(0, 0, self.width, self.height)];
        self.placeholder.userInteractionEnabled = NO;
        self.placeholder.backgroundColor = [UIColor clearColor];
        self.placeholder.font = font;
        self.placeholder.textColor = RGB(167, 181, 182);
        
        [self addSubview:self.placeholder];
    }
    self.placeholder.text = text;
    [self updatePlaceholder];
}

-(void)removePlaceholder{
    if (!self.placeholder){
        self.placeholder = [[UITextView alloc] initWithFrame:CGRectMake(0, 0, self.width, self.height)];
        self.placeholder.userInteractionEnabled = NO;
        self.placeholder.backgroundColor = [UIColor clearColor];

        self.placeholder.textColor = RGB(167, 181, 182);
        
        [self addSubview:self.placeholder];
    }

}

//
- (id)placeholder {
    return objc_getAssociatedObject(self, placeholderKey);
}

- (void)setPlaceholder:(id)newObjectTag {
    objc_setAssociatedObject(self, placeholderKey, newObjectTag, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}
//

//-(void)setText:(NSString *)text{
//    [self textChanged:self];
//}

-(void)textChanged:(UITextView *)textView{
    if (self.placeholder){
        self.placeholder.hidden = self.text.length>0;
    }
}

-(void)updatePlaceholder{
    [self textChanged:self];
}


@end
