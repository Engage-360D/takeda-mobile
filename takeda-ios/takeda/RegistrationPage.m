//
//  RegistrationPage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "RegistrationPage.h"

@interface RegistrationPage ()

@end

@implementation RegistrationPage
@synthesize scrollView;
@synthesize block_1;
@synthesize block_2;


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
    self.title = @"Cardiomagnil";
    self.block_1.frame = CGRectMake(0, 0, 320, self.view.frame.size.height - self.navigationController.navigationBar.frame.size.height - [[UIApplication sharedApplication] statusBarFrame].size.height);
    [self.scrollView addSubview:self.block_1];
    
    self.block_2.frame = CGRectMake(0, block_1.frame.size.height, 320, self.view.frame.size.height);
    [self.scrollView addSubview:self.block_2];
    
    [self.scrollView setContentSize:CGSizeMake(320, (self.view.frame.size.height - self.navigationController.navigationBar.frame.size.height - [[UIApplication sharedApplication] statusBarFrame].size.height)*2)];

}






-(IBAction)scrollContent:(id)sender{
    if ((int)[sender tag]==2) {// top
        CGPoint topOffset = CGPointMake(0, - self.navigationController.navigationBar.frame.size.height - [[UIApplication sharedApplication] statusBarFrame].size.height);
        [self.scrollView setContentOffset:topOffset animated:YES];
    }else{
        CGPoint bottomOffset = CGPointMake(0, self.scrollView.contentSize.height - self.scrollView.bounds.size.height);
        [self.scrollView setContentOffset:bottomOffset animated:YES];
    }
}














@end
