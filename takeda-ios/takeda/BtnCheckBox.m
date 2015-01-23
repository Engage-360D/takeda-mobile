//
//  BtnCheckBox.m
//  mmim
//
//  Created by SashOK on 8/9/14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import "BtnCheckBox.h"



@implementation BtnCheckBox

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}


-(id)initWithFrame:(CGRect)frame text:(NSString*)text uncheckedImage:(NSString*)unCheckedImage checkedImage:(NSString*)checkedImage ResultBlock:(void (^)(bool,id))ResultBlock{
    self = [super initWithFrame:frame];
    if (self) {
            [self setupText:text uncheckedImage:unCheckedImage checkedImage:checkedImage ResultBlock:ResultBlock];
        
           }
    return self;
}

-(void)setupText:(NSString*)text uncheckedImage:(NSString*)unCheckedImage checkedImage:(NSString*)checkedImage ResultBlock:(void (^)(bool,id))ResultBlock{
    self.contentHorizontalAlignment = UIViewContentModeTopLeft;
    self.imageView.contentMode = UIViewContentModeScaleAspectFit;
    self.adjustsImageWhenHighlighted = NO;
    _checkedImage = [UIImage imageNamed:checkedImage];
    _unCheckedImage = [UIImage imageNamed:unCheckedImage];
    if (text) _text = text; else _text = self.titleLabel.text;
    _resultBlock = ResultBlock;
    [self addTarget:self action:@selector(action:) forControlEvents:UIControlEventTouchUpInside];
    if (!_text_color) _text_color = RGB(57, 69, 77);
    if (!_text_font) _text_font = [UIFont fontWithName:@"Roboto-Regular" size:11];
    _textPadding = 5;
    [self setupCheckBox];

}

-(void)setupCheckBox{

    self.titleEdgeInsets = UIEdgeInsetsMake(0, _textPadding, 0, 0);
    [self setTitleColor:_text_color forState:UIControlStateNormal];
    [self setTitleColor:_text_color forState:UIControlStateSelected];
    [self.titleLabel setFont:_text_font];
    [self setTitle:_text forState:UIControlStateNormal];
    [self setImage:_unCheckedImage forState:UIControlStateNormal];
    [self setImage:_checkedImage forState:UIControlStateSelected];
    [self setNeedsDisplay];
}
/*
@property (nonatomic, strong) UIImage *checkedImage;
@property (nonatomic, strong) UIImage *unCheckedImage;
@property (nonatomic, strong) NSString *text;
@property (nonatomic, strong) UIFont *text_font;
@property (nonatomic, strong) UIColor *text_color;
@property (nonatomic) float textPadding;
*/

-(void)setCheckedImage:(UIImage *)checkedImage{
    _checkedImage = checkedImage;
    [self setImage:_checkedImage forState:UIControlStateSelected];
}

-(void)setUnCheckedImage:(UIImage *)unCheckedImage{
    _unCheckedImage = unCheckedImage;
    [self setImage:_unCheckedImage forState:UIControlStateNormal];
}

-(void)setText:(NSString *)text{
    _text = text;
    [self setTitle:_text forState:UIControlStateNormal];
}

-(void)setText_font:(UIFont *)text_font{
    _text_font = text_font;
    [self.titleLabel setFont:_text_font];
}

-(void)setText_color:(UIColor *)text_color{
    _text_color = text_color;
    [self setTitleColor:_text_color forState:UIControlStateNormal];
    [self setTitleColor:_text_color forState:UIControlStateSelected];
}

-(void)setTextPadding:(float)textPadding{
    _textPadding = textPadding;
    self.titleEdgeInsets = UIEdgeInsetsMake(0, _textPadding, 0, 0);
}

-(void)changeButtonState:(bool)state
{
    self.selected = state;
}

-(void)action:(BtnCheckBox*)sender{
    self.selected = !self.selected;
    _resultBlock(self.selected,_target);
}

-(void)setImageHeight:(float)imageHeight{
    self.imageView.autoresizingMask = (UIViewAutoresizingNone); //пока не работает (((
    
    
    _imageHeight = imageHeight;
    self.imageView.frame = CGRectMake(self.imageView.frame.origin.x, self.imageView.frame.origin.y, imageHeight, imageHeight);
    [self setNeedsDisplay];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
