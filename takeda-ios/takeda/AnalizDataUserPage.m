//
//  AnalizDataUserPage.m
//  takeda
//
//  Created by Serg on 4/2/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "AnalizDataUserPage.h"

@interface AnalizDataUserPage ()

@end

@implementation AnalizDataUserPage
@synthesize nextStepBtn;
@synthesize titleRisk;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self.nextStepBtn setBackgroundImage:[[UIImage imageNamed:@"button_arrow_bg"] resizableImageWithCapInsets:UIEdgeInsetsMake(10, 10, 10, 30)] forState:UIControlStateNormal];
}



@end
